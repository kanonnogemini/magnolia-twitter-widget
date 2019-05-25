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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.jcr.Node;

import org.apache.commons.lang3.StringUtils;
import org.magnolia.twitter.widget.MgnlTwitterWidget;
import org.magnolia.twitter.widget.utils.UtilsDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.magnolia.objectfactory.Components;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.module.cache.Cache;
import info.magnolia.module.cache.inject.CacheFactoryProvider;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * The Class ModelTwitter.
 */
public class ModelTwitter extends RenderingModelImpl<RenderableDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelTwitter.class);

    private static final Integer UNO = 1;
    private static final Integer COUNT_TWEETS_DEFAULT = 5;
    private String followText = "https://twitter.com/{0}?ref_src=twsrc%5Etfw";
    private String inicioFollow = "<a href='";
    private String finalFollow = "' class='twitter-follow-button' data-show-count='false' data-show-screen-name='false' data-lang='es'>Síguenos</a>";

    private final Provider<CacheFactoryProvider> cacheFactoryProvider;

    /** The utils date. */
    private UtilsDate utilsDate;
    private MgnlTwitterWidget bean;

    @Inject
    public ModelTwitter(Node content, RenderableDefinition definition, RenderingModel<?> parent, UtilsDate utilsDate,
            Provider<CacheFactoryProvider> cacheFactoryProvider) {
        super(content, definition, parent);
        this.utilsDate = utilsDate;
        this.cacheFactoryProvider = cacheFactoryProvider;
    }

    // Creamos la conexion con twitter
    private Twitter getTwitterInstance() {

        Twitter twitter = null;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        bean = Components.getComponent(MgnlTwitterWidget.class);

        if (null != bean && !StringUtils.isEmpty(bean.getConsumerKey())
                && !StringUtils.isEmpty(bean.getConsumerSecret()) && !StringUtils.isEmpty(bean.getAccessTokenKey())
                && !StringUtils.isEmpty(bean.getAccessTokenSecret())) {
            cb.setDebugEnabled(true).setOAuthConsumerKey(bean.getConsumerKey())
                    .setOAuthConsumerSecret(bean.getConsumerSecret()).setOAuthAccessToken(bean.getAccessTokenKey())
                    .setOAuthAccessTokenSecret(bean.getAccessTokenSecret());

            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();
        }
        return twitter;
    }

    public TwitterDTO recuperarTweets() {

        LOGGER.info("ENTRA en el método recuperarTweets");

        Cache cache = cacheFactoryProvider.get().get().getCache("twitter_cyc_webagentes_cache");
        TwitterDTO twitterDTO=(TwitterDTO)cache.getQuiet("twitterDTO");
        
        if (twitterDTO==null)
        {
            int totalTweets = COUNT_TWEETS_DEFAULT;
            try {
                totalTweets = Integer.parseInt(bean.getCountTweets());
            } catch (NumberFormatException e) {
                LOGGER.error("ERROR al parsear el entero: {}", bean.getCountTweets());
            }
            
            Paging paging = new Paging(UNO, totalTweets);
            List<Status> statuses = null;
            List<Tweet> tweetList = new ArrayList<>();
            User user = null;
            
            Twitter twitter = getTwitterInstance();
            if (null != twitter) {

                
                try {
                    user = twitter.verifyCredentials();
                    if (null != user) {
                        String id = null;
                        String texto = null;
                        String fechaDePosteo = null;
                        Date fechaDate = null;
                        twitterDTO = new TwitterDTO();

                        LOGGER.info("Pefil de usuario Twitter al que se le recuperarán los tweets: {}",
                                user.getScreenName());
                        LOGGER.info("Número de tweets que se le recuperarán: {}", totalTweets);

                        // Recuperamos los datos del perfil del usuario.
                        twitterDTO.setPerfil(user.getScreenName());
                        twitterDTO.setNombreDelPerfil(user.getName());
                        twitterDTO.setImagenPefilUrl(user.getProfileImageURLHttps());
                        twitterDTO.setFollowHtml(
                                this.formatearTextoFollowHTML(followText, new Object[] { user.getScreenName() }));

                        // Recuperamos los tweets del usuario.
                        statuses = twitter.getUserTimeline(paging);
                        for (Status status : statuses) {
                            Tweet tweet = null;
                            id = String.valueOf(status.getId());
                            texto = status.getText();
                            fechaDePosteo = utilsDate.convertirDateToStringPatronTwitter(status.getCreatedAt());
                            fechaDate = status.getCreatedAt();
                            tweet = new Tweet(id, texto, fechaDePosteo, fechaDate);

                            LOGGER.info("{}:{}", status.getUser().getName(), status.getText());
                            String url = "https://twitter.com/" + status.getUser().getScreenName() + "/status/"
                                    + status.getId();
                            LOGGER.info("Above tweet URL : {}", url);

                            tweetList.add(tweet);
                        }
                        twitterDTO.setTweets(tweetList);
                        cache.put("twitterDTO", twitterDTO, 1800);
                    }
                } catch (TwitterException e) {
                    LOGGER.error("ERROR al recuperar los Tweets de {}: {}", user, e);
                }
            }

            LOGGER.info("SALE del métod recuperarTweets");
        }
        
        return twitterDTO;
    }

    private String formatearTextoFollowHTML(String patron, Object[] parametros) {

        String textoFormateado = formatearTexto(patron, parametros);
        return inicioFollow.concat(textoFormateado).concat(finalFollow);
    }

    private String formatearTexto(String patron, Object[] parametros) {

        String textoFormateado = null;

        MessageFormat textoPatron = new MessageFormat(patron);
        textoFormateado = textoPatron.format(parametros);

        return textoFormateado;
    }

}