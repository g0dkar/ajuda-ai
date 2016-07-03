try {
	(function ($, autosize) {
		Number.prototype.formatMoney = function (c, d, t) {
			var n = this, 
			c = isNaN(c = Math.abs(c)) ? 2 : c, 
			d = d == undefined ? "," : d, 
			t = t == undefined ? "." : t, 
			s = n < 0 ? "-" : "", 
			i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", 
			j = (j = i.length) > 3 ? j % 3 : 0;
			return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
		};
		
			 
		$(function () {
			var institutionSlug = (function () { var nextSlash = location.pathname.indexOf("/", 1); return nextSlash > 0 ? location.pathname.substring(0, nextSlash) : location.pathname; })();
			$("#page-content").addClass("loaded");
			$(".slider").slick();
			$("textarea").each(function () { autosize(this); });
			
			var tiers = $(".institution-page .donation-tier:not(.donation-tier-custom)");
			var changeTimeout = null;
			var valueInput = $(".donation-tier-custom input").on("focus", function () {
				$(this).change();
			}).on("change", function () {
				var val = parseInt(this.value);
				if (val) {
					$("#addcosts-cc").html("$" + ((Math.ceil((val * 100 + 65) / 0.9451) - val * 100) / 100).toFixed(2) + " (cartão de crédito)");
					$("#addcosts-others").html("$" + ((Math.ceil((val * 100 + 65) / 0.9651) - val * 100) / 100).toFixed(2) + " (outras formas de pagamento)");
					tiers.each(function (i) {
						var $this = $(this), tierVal = parseInt($this.data("value"));
						if (tierVal) {
							if (i == 0 || val >= tierVal) {
								$this.addClass("selected");
								if (i == 0 && val < tierVal) {
									$this.find(".donation-value").html("$" + val);
								}
								else {
									$this.find(".donation-value").html("$" + tierVal + "+");
								}
							}
							else {
								$this.removeClass("selected");
							}
						}
					});
				}
			});
			valueInput.change();
			tiers.on("click tap", function () {
				valueInput.val($(this).data("value")).change();
			});
			
			var token = $(".institution-page #token").val();
			$(".institution-page #email").on("change", function () {
				var val = this.value;
				if (val) {
					if (changeTimeout) { clearTimout(changeTimeout); }
					setTimeout(function () {
						$.getJSON(institutionSlug + "/api/helper?t=" + token + "&e=" + encodeURIComponent(val), function (data) {
							if (data.name) { $(".institution-page #name").val(data.name); }
						}).always(function () {
							$(".institution-page #name").attr("disabled", false);
						});
					}, 250);
				}
			});
			
			var institutionDataRows = $(".institution-data-row");
			if (institutionDataRows.length > 0) {
				var tryGetData = function () {
					$.getJSON(institutionSlug + "/api/info-doacoes", function (data) {
						institutionDataRows.each(function () {
							var $this = $(this);
							var helperCount = $this.find(".helper-count");
							var donationsCount = $this.find(".donations-count");
							helperCount.html(data.count);
							donationsCount.html("$ " + data.value.formatMoney(2));
						});
					}).fail(function () {
						institutionDataRows.each(function () {
							var $this = $(this);
							$this.find(".helper-count").html("...");
							$this.find(".donations-count").html("$ ...");
							setTimeout(tryGetData, 1000);
						});
					});
				}
				tryGetData();
			}
			
			$(".facebook-share-link").on("click tap", function (event) {
				var $this = $(this);
				event.preventDefault();
				
				if (FB && FB.ui) {
					FB.ui({
						method: "share",
						mobile_iframe: true,
						href: $this.data("href"),
						quote: $this.data("quote"),
						hashtag: $this.data("hashtag")
					});
				}
				
				return false;
			});
		});
	})(jQuery, autosize);
} catch (e) {
	document.getElementById("page-content").className = "loaded";
}