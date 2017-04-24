module.exports = function(grunt) {
	// 1. Time how long tasks take
	require("time-grunt")(grunt);
	
	// 2. load all grunt tasks matching the `grunt-*` pattern
	require("load-grunt-tasks")(grunt);
	
	// 3. All configuration goes here
	grunt.initConfig({
		path: {
			src: "src",					// Dev files
			dest: "dist",				// Dist files
			bower: "bower_components"	// Bower Files
		},
		concat: {
			options: {
				sourceMap: true
			},
			js: {
				src: [
					"<%= path.bower %>/angular/angular.js",
					"<%= path.bower %>/angular-animate/angular-animate.js",
					"<%= path.bower %>/angular-sanitize/angular-sanitize.js",
					"<%= path.bower %>/angular-ui-router/release/angular-ui-router.js",
					"<%= path.bower %>/showdown/dist/showdown.js",
					"<%= path.bower %>/ng-showdown/dist/ng-showdown.js",
					"<%= path.bower %>/angular-easyfb/build/angular-easyfb.js",
					"<%= path.bower %>/angular-recaptcha/release/angular-recaptcha.js",
					"<%= path.src %>/js/ajudaai.js"
				],
				dest: "<%= path.src %>/js/ajuda-ai-1.0.0.js",
			},
			css: {
				src: [
					"<%= path.bower %>/bootstrap/dist/css/bootstrap.css",
					"<%= path.src %>/css/ajudaai.css"
				],
				dest: "<%= path.src %>/css/ajuda-ai-1.0.0.css",
			}
		},
		
		uglify: {
			options: {
				sourceMap: true,
				sourceMapIncludeSources: true,
				sourceMapIn: "<%= path.src %>/js/ajuda-ai-1.0.0.js.map",
				compress: { drop_console: true }
			},
			dev: {
				files: {
					"<%= path.dest %>/js/ajuda-ai-1.0.0.js": ["<%= path.src %>/js/ajuda-ai-1.0.0.js"]
				}
			}
		},
		
		cssnano: {
			dist: {
				files: {
					"<%= path.dest %>/css/ajuda-ai-1.0.0.css": "<%= path.src %>/css/ajuda-ai-1.0.0.css"
				}
			}
		},
		
		imagemin: {
			dynamic: {										// Another target
				files: [{
					expand: true,							// Enable dynamic expansion
					cwd: "<%= path.src %>/img/",			// src matches are relative to this path
					src: ["**/*.{png,jpg,gif}"],			// Actual patterns to match
					dest: "<%= path.dest %>/img/"			// Destination path prefix
				}]
			}
		},
		
		htmlmin: {
			dist: {
				options: {
					removeComments: true,
					collapseWhitespace : true,
					minifyCSS: true,
					minifyJS: true,
					removeRedundantAttributes: true
				},
				files: [{
					expand: true,
					cwd: "<%= path.src %>",
					src: ["**/*.html"],				// copy all files and subfolders ending in .html
					dest: "<%= path.dest %>",		// destination folder
				}]
			}
		},
		

		connect: {
			server: {
				options: {
					port: 8000,
					base: "dist"
				}
			}
		},
		
		watch: {
			js: {
				files: "<%= path.src %>/js/**/*.js",
				tasks: ["concat:js", "uglify"]
			},
			css: {
				files: "<%= path.src %>/css/**/*.css",
				tasks: ["concat:css", "cssnano"],
			},
			img: {
				files: "<%= path.src %>/img/**/*.{png,jpg,gif}",
				tasks: ["imagemin"],
			},
			html: {
				files: "<%= path.src %>/**/*.html",
				tasks: ["htmlmin:dist"],
			}
		}
	});

	// 4. Where we tell Grunt what to do when we type "grunt" into the terminal.
	grunt.registerTask("default", ["concat:js", "uglify", "concat:css", "cssnano", "imagemin", "htmlmin", "connect", "watch"]);
};