package org.akash.messcup.service;

import javax.smartcardio.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.*;

public class CardReader implements Runnable {

    private final Consumer<String> onUidRead;

    /* ===================== LOGGER ===================== */
    private static final Logger LOGGER = Logger.getLogger(CardReader.class.getName());

    static {
        try {
            FileHandler fh = new FileHandler("logs/messcup.log", 0, 1, true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace(); // fallback
        }
    }

    public CardReader(Consumer<String> onUidRead) {
        this.onUidRead = onUidRead;
    }

    @Override
    public void run() {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();

            if (terminals.isEmpty()) {
                onUidRead.accept("NO_READER");
                return;
            }

            CardTerminal terminal = terminals.get(0);
            LOGGER.info("Smart card reader detected: " + terminal.getName());

            while (true) {
                terminal.waitForCardPresent(0);
                LOGGER.fine("Card present detected");

                try {
                    Card card = terminal.connect("*");
                    CardChannel channel = card.getBasicChannel();

                    byte[] cmd = {
                            (byte) 0xFF,
                            (byte) 0xCA,
                            (byte) 0x00,
                            (byte) 0x00,
                            (byte) 0x00
                    };

                    ResponseAPDU response = channel.transmit(new CommandAPDU(cmd));

                    if (response.getSW() == 0x9000) {
                        String uid = bytesToHex(response.getData());
                        onUidRead.accept(uid);
                    } else {
                        LOGGER.warning("Failed to read card UID, SW=" + Integer.toHexString(response.getSW()));
                        onUidRead.accept("ERROR");
                    }

                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error reading card", e);
                    onUidRead.accept("ERROR");
                }

                terminal.waitForCardAbsent(0);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "CardReader initialization failed", e);
            onUidRead.accept("ERROR");
        }
    }

    private String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
