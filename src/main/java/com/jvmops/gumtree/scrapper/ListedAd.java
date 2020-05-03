package com.jvmops.gumtree.scrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@Builder
class ListedAd {
//    private ObjectId id;
    private String city;
    private String gumtreeId;
    private String url;
    private String title;
    private Integer price;
    private boolean featured;
}
