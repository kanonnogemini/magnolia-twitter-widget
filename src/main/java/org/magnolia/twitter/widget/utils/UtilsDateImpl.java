/**
 * This file Copyright (c) 2015-2019 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package org.magnolia.twitter.widget.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsDateImpl implements UtilsDate {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1673541998954490057L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsDateImpl.class);

    private static final String PATRON_FECHA_TWITTER = "dd MMMM yyyy";

    private static final String CARACTER_ESPACIO = " ";

    @Override
    public String convertirDateToStringPatronTwitter(Date fecha) {

        String fechaString = null;
        LOGGER.info("ENTRA en el método convertirDateToStringPatronTwitter");

        String fechaTwitter = convertirDateToStringConPatron(fecha, PATRON_FECHA_TWITTER);
        String[] partesFechaTwitter = fechaTwitter.split(CARACTER_ESPACIO);

        fechaString = partesFechaTwitter[0] + CARACTER_ESPACIO + partesFechaTwitter[1].substring(0, 1).toUpperCase()
                + partesFechaTwitter[1].substring(1) + CARACTER_ESPACIO + partesFechaTwitter[2];

        LOGGER.info("SALE del método convertirDateToStringPatronTwitter con fecha: {}", fechaString);
        return fechaString;
    }

    @Override
    public String convertirDateToStringConPatron(Date fecha, String patron) {

        LOGGER.info("ENTRA en el método convertirDateToStringConPatron con fecha: {} y patrón: {}", fecha, patron);

        SimpleDateFormat sdf = new SimpleDateFormat(patron);
        return sdf.format(fecha);
    }

}
