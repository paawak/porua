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
import java.sql.SQLException;
import java.sql.Statement;

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
    public int saveUrl(String url) {
        String sql = "insert into audit_web_site (site_name) values (?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, url);
                return ps;
            }
        }, generatedKeyHolder);
        return generatedKeyHolder.getKey().intValue();
    }

    @Override
    public void insertBanglaWord(int urlId, String token) {
        String sql = "insert into bangla_word (audit_web_site_id, word) values (?, ?)";
        jdbcOperations.update(sql, urlId, token);
    }

    @Override
    public boolean doesUrlExist(String url) {
        String sql = "select count(*) from audit_web_site where site_name = ?";
        return jdbcOperations.queryForObject(sql, Integer.class, url) == 1;
    }

}
