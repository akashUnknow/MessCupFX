package org.akash.messcup.service;


import javax.smartcardio.*;
import java.util.List;
import java.util.function.Consumer;

public class CardReader implements Runnable {

    private final Consumer<String> onUidRead;

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

            while (true) {
                terminal.waitForCardPresent(0);

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
                }

                card.disconnect(false);
                terminal.waitForCardAbsent(0);
            }

        } catch (Exception e) {
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
