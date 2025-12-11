package org.akash.messcup.service;

import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import org.akash.messcup.menuDto.MenuDto;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class MenuService {

    public List<MenuDto> setMenu() {
        List<MenuDto> menuList=new ArrayList<>();
        String filePath="C:/Mess/menu.txt";
        try {
            List<String> lines= Files.readAllLines(Paths.get(filePath));

            for (String line:lines){
                String[] parts=line.split("\\|");
                if(parts.length==4){
                    menuList.add(new MenuDto(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            return menuList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
