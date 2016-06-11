<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page>
	<div id="page-index" ng-controller="MainController">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
					<div class="hero">
						<uib-carousel active="active" interval="currentSlideInterval()">
							<uib-slide ng-repeat="slide in slides track by slide.id" index="slide.id">
								<img ng-src="{{::slide.image}}" style="margin:auto;">
								<div class="carousel-caption">
									<h1 ng-bind-html="::slide.caption"></h1>
									<p class="hero-btn-para"><a ng-href="{{::slide.href}}" class="hero-btn" ng-bind-html="::slide.button"></a></p>
								</div>
							</uib-slide>
						</uib-carousel>
					</div>
				</div>
			</div>
		</div>
		
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<div class="page-content">
						<h1 class="page-title">Algum texto</h1>
						<h4 class="page-subtitle">Algum texto vai ficar aqui. Por enquanto, temos Slipsum.</h4>
						<hr>
						<p>Normally, both your asses would be dead as fucking fried chicken, but you happen to pull this shit while I'm in a transitional period so I don't wanna kill you, I wanna help you. But I can't give you this case, it don't belong to me. Besides, I've already been through too much shit this morning over this case to hand it over to your dumb ass. </p>
						<p>Well, the way they make shows is, they make one show. That show's called a pilot. Then they show that show to the people who make shows, and on the strength of that one show they decide if they're going to make more shows. Some pilots get picked and become television programs. Some don't, become nothing. She starred in one of the ones that became nothing. </p>
						<p>Your bones don't break, mine do. That's clear. Your cells react to bacteria and viruses differently than mine. You don't get sick, I do. That's also clear. But for some reason, you and I react the exact same way to water. We swallow it too fast, we choke. We get some in our lungs, we drown. However unreal it may seem, we are connected, you and I. We're on the same curve, just on opposite ends. </p>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>