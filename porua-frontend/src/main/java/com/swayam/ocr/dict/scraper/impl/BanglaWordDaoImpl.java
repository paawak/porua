/*
 * BanglaWordDaoImpl.java
 *
 * Created on 12-Nov-2016 11:22:43 PM
 *
 * Copyright (c) 2002 - 2008 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */

package com.swayam.ocr.dict.scraper.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.swayam.ocr.dict.scraper.api.BanglaWordDao;

/**
 * 
 * @author paawak
 */
@Repository
public class BanglaWordDaoImpl implements BanglaWordDao {

    private final JdbcOperations jdbcOperations;

    @Autowired
    public BanglaWordDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public int saveUrl(int parentId, String url) {
        String sql = "insert into audit_web_site (parent_id, site_name) values (?, ?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, parentId);
                ps.setString(2, url);
                return ps;
            }
        }, generatedKeyHolder);
        return generatedKeyHolder.getKey().intValue();
    }

    @Override
    public void insertBanglaWord(String token) {
        String sql = "insert into bangla_word ( word) values ( ?)";
        jdbcOperations.update(sql, token);
    }

    @Override
    public boolean doesUrlExist(String url) {
        String sql = "select count(*) from audit_web_site where site_name = ?";
        return jdbcOperations.queryForObject(sql, Integer.class, url) == 1;
    }

    @Override
    public AuditWebsite getAuditWebsite(String url) {
        String sql = "select * from audit_web_site where site_name = ?";
        return jdbcOperations.query(sql, new Object[] { url }, (ResultSet rs, int row) -> {
            int parentId = rs.getInt("parent_id");
            Optional<Integer> optionalParentId;
            if (parentId == -1) {
                optionalParentId = Optional.empty();
            } else {
                optionalParentId = Optional.of(parentId);
            }
            return new AuditWebsite(rs.getInt("id"), optionalParentId, rs.getString("site_name"),
                    rs.getBoolean("scraping_completed"));
        }).get(0);
    }

    @Override
    public void markScrapingCompleted(int baseUrlId) {
        String sql = "update audit_web_site set scraping_completed = TRUE where id = ?";
        jdbcOperations.update(sql, baseUrlId);
    }

}
