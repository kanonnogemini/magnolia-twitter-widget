[#assign twitter = model.recuperarTweets()!]
[#assign class = content.class!false]
<!-- Twitter Widget -->
<script type="text/javascript" async src="https://platform.twitter.com/widgets.js"></script>

	<div class="container">
		<div class="row">
		[#if twitter?has_content]
			<div class="col-md-12">
				<div class="area-twitter">
					<img src="${twitter.imagenPefilUrl}" alt="${twitter.nombreDelPerfil}">
					<div class="twitterContent">
						<div class="twitterCount">
							<span class="profileName">${twitter.nombreDelPerfil}</span>
							<span class="profile">@${twitter.perfil}</span>
							<div class="followBtn">${twitter.followHtml}</div>
						</div>
						[#if twitter.tweets?has_content]
						<div class="list-tweets carousel slide" data-ride="carousel">
							<div class="carousel-inner" role="listbox">
								[#list twitter.tweets as tweet]
									<div class="item-tweets item [#if tweet?counter == 1]active[/#if]">
										<p class="tweetText">${tweet.texto}</p>
										<div class="dateActions">
											<span>${tweet.fechaDePosteo}</span>
											<div class="tweetActions">
												<a class="tweetLink" href="https://twitter.com/intent/tweet?in_reply_to=${tweet.id}"><i class="fa fa-reply"></i>Reply</a>
												<a class="tweetLink" href="https://twitter.com/intent/retweet?tweet_id=${tweet.id}"><i class="fa fa-retweet"></i>Retweet</a>
												<a class="tweetLink" href="https://twitter.com/intent/like?tweet_id=${tweet.id}"><i class="fa fa-star"></i>Favorite</a>
											</div>
										</div>
									</div>
								[/#list]
							</div>
						</div>
						[/#if]
					</div>
				</div>
			</div>
		[/#if]
		</div>
	</div>

