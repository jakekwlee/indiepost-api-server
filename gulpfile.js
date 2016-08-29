var gulp = require('gulp');
var babel = require('gulp-babel');
var sourcemaps = require('gulp-sourcemaps');
var jshint = require('gulp-jshint');
var exec = require('child_process').exec;

gulp.task('babel', function() {
    return gulp.src('./migration/src/**/*.js')
        .pipe(sourcemaps.init())
        .pipe(babel())
        .pipe(sourcemaps.write('./', {
            sourceRoot: '../src'
        }))
        .pipe(gulp.dest('./migration/dist'));
});

gulp.task('lint', function() {
    return gulp.src('./migration/dist/**/*.js')
        .pipe(jshint());
});

gulp.task('migrate', ['babel', 'lint'], function(callback) {
    exec('node ./migration/dist/app.js', function(err, stdout, stderr) {
        console.log(stdout);
        console.log(stderr);
        callback(err);
    });
});