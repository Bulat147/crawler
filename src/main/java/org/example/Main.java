package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Crawler crawler = new CrawlerRealisation("files");

        List<String> urls = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/urls.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                urls.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        crawler.setUrlList(urls);
        crawler.downloadPages();
    }
}