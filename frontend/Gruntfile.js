module.exports = function(grunt) {

    // 1. Time how long tasks take
    require('time-grunt')(grunt);

    // 2. load all grunt tasks matching the `grunt-*` pattern
    require('load-grunt-tasks')(grunt);

    // 3. All configuration goes here
    grunt.initConfig({
        imagemin: {
          dynamic: {                         // Another target
            files: [{
              expand: true,                  // Enable dynamic expansion
              cwd: 'dev/img/',                   // Src matches are relative to this path
              src: ['**/*.{png,jpg,gif}'],   // Actual patterns to match
              dest: 'dist/img/'                  // Destination path prefix
            }]
          }
        },
        copy: {
          main: {
            files: [
              {expand: true, flatten: true, src: ['dev/css/**'], dest: 'dist/css/', filter: 'isFile'},
              {expand: true, flatten: true, src: ['dev/js/**'], dest: 'dist/js/', filter: 'isFile'}
            ]
          },
        },
        includereplace: {
          html: {
            src: '*.html',
            dest: 'dist/'
          }
        },
        watch: {
          options: {
            livereload: true,
          },
          css: {
            files: 'dev/css/**/*.css',
            tasks: ['copy:main'],
          },
          js: {
            files: 'dev/js/**/*.js',
            tasks: ['copy:main'],
          },
          img: {
            files: 'dev/img/**/*.{png,jpg,gif}',
            tasks: ['imagemin'],
          },
          html:{
            files: ['*.html', 'includes/**/*.html'],
            tasks: ['includereplace:html']
          },
        },
        connect: {
          server: {
            options: {
              port: 8000,
              base: './'
            }
          }
        }
    });

    // 4. Where we tell Grunt what to do when we type "grunt" into the terminal.
    grunt.registerTask('dev', ['connect', 'watch']);
};