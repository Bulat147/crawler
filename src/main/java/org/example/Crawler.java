package org.example;

import java.util.List;

public interface Crawler {

    void setUrlList(List<String> urls);

    boolean downloadPages();

}
