/*
 * Language.java
 *
 * Created on Oct 19, 2011 7:44:48 PM
 *
 * Copyright (c) 2002 - 2011 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */

package com.swayam.ocr.core.util;

import java.io.Serializable;

/**
 * 
 * @author paawak
 */
public enum Script implements Serializable {

    BANGLA, DEVNAGARI, ROMAN;
    
    @Override
	public String toString() {
		return name();
	}

}
