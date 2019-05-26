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
package org.magnolia.twitter.widget.models;

import java.io.Serializable;
import java.util.Date;

public class Tweet implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2930681704729864797L;
    private String id;
    private String texto;
    private String fechaDePosteo;
    private Date fechaDate;

    public Tweet(String id, String texto, String fechaDePosteo, Date fechaDate) {
        super();
        this.id = id;
        this.texto = texto;
        this.fechaDePosteo = fechaDePosteo;
        this.fechaDate = fechaDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFechaDePosteo() {
        return fechaDePosteo;
    }

    public void setFechaDePosteo(String fechaDePosteo) {
        this.fechaDePosteo = fechaDePosteo;
    }

    public Date getFechaDate() {
        return fechaDate;
    }

    public void setFechaDate(Date fechaDate) {
        this.fechaDate = fechaDate;
    }

    @Override
    public String toString() {
        return "Tweet [id=" + id + ", texto=" + texto + ", fechaDePosteo=" + fechaDePosteo + ", fechaDate=" + fechaDate
                + "]";
    }

}
