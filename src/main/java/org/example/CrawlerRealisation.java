package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CrawlerRealisation implements Crawler {

    private List<String> urls = new ArrayList<>();
    private String pagesPath;
    private AtomicInteger index = new AtomicInteger(0);

    public CrawlerRealisation(String pagesPath) {
        this.pagesPath = pagesPath;
        (new File(pagesPath)).mkdirs();
        initIndexFile(pagesPath + "/index.txt");
    }

    @Override
    public void setUrlList(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public boolean downloadPages() {
        /* сходить по урлам
        выкачать оттуда содержимое html
        сохранить страницы в файлы
        * */
        Document document;
        for (String url: urls) {
            try {
                document = Jsoup.connect(url).get();
                Integer saveFileNumber = saveToFile(document.html());
                if (saveFileNumber == null) {
                    return false;
                }
                if (!saveToIndexFile(saveFileNumber, url)) {
                    return false;
                }
            } catch (IOException e) {
                System.out.println("Не удалось обработать ссылку - " + url);
                return false;
            }
        }

        return true;
    }

    public boolean saveToIndexFile(Integer number, String url) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pagesPath + "/index.txt", true))) {
            writer.write("%s -> %s\n".formatted(number, url));
        } catch (IOException e) {
            System.out.printf("Не удалось сохранить ссылку в index.txt под номером - %s\n", number);
            return false;
        }

        System.out.printf("Ссылка сохранена в index.txt под номером - %s\n", number);
        return true;
    }

    public Integer saveToFile(String value) {
        int number = index.addAndGet(1);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pagesPath + "/" + number + ".html"))) {
            writer.write(value); // Записываем HTML-код в файл
        } catch (IOException e) {
            System.out.printf("Не удалось сохранить страницу в файл с номером - %s\n", number);
            return null;
        }

        System.out.printf("Страница сохранена в файл с номером - %s\n", number);
        return number;
    }

    public void initIndexFile(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            System.out.println("Файл index.txt инициализирован");
        } catch (IOException e) {
            System.err.println("Ошибка при инициалиации файла index.txt: " + e.getMessage());
        }
    }

}
