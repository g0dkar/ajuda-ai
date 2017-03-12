module.exports = function(grunt) {
	// 1. Time how long tasks take
	require("time-grunt")(grunt);
	
	// 2. load all grunt tasks matching the `grunt-*` pattern
	require("load-grunt-tasks")(grunt);
	
	// 3. All configuration goes here
	grunt.initConfig({
		path: {
			src: "src",			// Dev files
			dest: "dist",		// Dist files
		},
		concat: {
			options: {
				sourceMap: true
			},
			js: {
				src: [
					"<%= path.src %>/bower_components/angular/angular.js",
					"<%= path.src %>/bower_components/angular-animate/angular-animate.js",
					"<%= path.src %>/js/ajudaai.js"
				],
				dest: "<%= path.src %>/js/ajudaai.js",
			},
			css: {
				src: [
					"<%= path.src %>/css/reset.css",
					"<%= path.src %>/css/ajudaai.css"
				],
				dest: "<%= path.src %>/css/ajudaai.css",
			}
		},
		
		uglify: {
			options: {
				sourceMap: true,
				sourceMapIncludeSources: true,
				sourceMapIn: "<%= path.src %>/js/ajudaai.js.map",
				compress: { drop_console: true }
			},
			dev: {
				files: {
					"<%= path.dest %>/js/ajudaai.js": ["<%= path.src %>/js/ajudaai.js"]
				}
			}
		},
		
		cssnano: {
			dist: {
				files: {
					"<%= path.dest %>/css/ajudaai.css": "<%= path.src %>/css/ajudaai.css"
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
		
		copy: {
			html: {
				src: "<%= path.src %>/**/*.html",	// copy all files and subfolders **with ending .html**
				dest: "<%= path.dest %>/",			// destination folder
//				expand: true				// required when using cwd
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
				tasks: ["copy:html"],
			}
		}
	});

	// 4. Where we tell Grunt what to do when we type "grunt" into the terminal.
	grunt.registerTask("default", ["concat:js", "uglify", "concat:css", "cssnano", "imagemin", "copy"/*, "watch"*/]);
};