//package com.example.BloodDonationSupportSystem.entity;
//
//import com.example.BloodDonationSupportSystem.enumentity.ArticleStatusEnum;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import lombok.Data;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.type.SqlTypes;
//
//import java.util.Date;
//
//@Data
//@Entity(name = "article")
//public class Article {
//    @Id
//    @GeneratedValue
//    @JdbcTypeCode(SqlTypes.UUID)
//    @Column(name = "article_id", columnDefinition = "uniqueidentifier")
//    private String articleId;
//
//    @Column(name = "content")
//    private String content;
//
//    @Column(name = "created_at")
//    private Date createdAt;
//
//
//
//
//    @Column(name = "status")
//    private ArticleStatusEnum status;
//
//}
