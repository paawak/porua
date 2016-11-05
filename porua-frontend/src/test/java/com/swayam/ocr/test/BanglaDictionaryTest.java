/*
 * BanglaDictionaryTest.java
 *
 * Created on 06-Nov-2016 12:05:09 AM
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

package com.swayam.ocr.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

/**
 * 
 * @author paawak
 */
public class BanglaDictionaryTest {

    @Test
    public void test() throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bangla_dictionary?useUnicode=true&amp;amp;characterEncoding=utf-8&amp;amp;autoReconnect=true",
                "root", "Root123!");

        String sql = "select *  from dict_table where id=1";
        PreparedStatement stat = con.prepareStatement(sql);

        ResultSet res = stat.executeQuery();

        while (res.next()) {
            System.out.println(
                    "BanglaDictionaryTest.test() " + res.getString("bn_word"));
        }

    }

}
