try {
	(function ($, autosize) {
		$(function () {
			$("#page-content").addClass("loaded");
			$(".slider").slick();
			$("textarea").each(function () { autosize(this); });
			
			var tiers = $(".donation-tier:not(.donation-tier-custom)");
			var changeTimeout = null;
			var valueInput = $(".donation-tier-custom input").on("focus", function () {
				$(this).change();
			}).on("change", function () {
				var val = parseInt(this.value);
				if (val) {
					if (changeTimeout) { clearTimout(changeTimeout); }
					setTimeout(function () {
						$("#addcosts-amount").html();
						$("#addcosts-cc").html("$" + ((Math.ceil((val * 100 + 65) / 0.9451) - val * 100) / 100).toFixed(2) + " (cartão de crédito)");
						$("#addcosts-others").html("$" + ((Math.ceil((val * 100 + 65) / 0.9651) - val * 100) / 100).toFixed(2) + " (outras formas de pagamento)");
						tiers.each(function (i) {
							var tierVal = parseInt($(this).data("value"));
							if (tierVal) {
								if (i == 0 || val >= tierVal) {
									$(this).addClass("selected");
									if (i == 0 && val < tierVal) {
										$(this).find(".donation-value").html("$" + val);
									}
									else {
										$(this).find(".donation-value").html("$" + tierVal);
									}
								}
								else {
									$(this).removeClass("selected");
								}
							}
						});
					}, 200);
				}
			});
			valueInput.change();
			tiers.on("click", function () {
				valueInput.val($(this).data("value")).change();
			});
			
			$("#anonymous").on("change", function () {
				if (this.checked) {
					$("#name,#email").attr("disabled", true).val("");
				}
				else {
					$("#name,#email").attr("disabled", false);
				}
			});
			
			$("#addcosts").on("change", function () { $("#addcoststype").attr("disabled", !this.checked); })
		});
	})(jQuery, autosize);
} catch (e) {
	document.getElementById("page-content").className = "loaded";
}