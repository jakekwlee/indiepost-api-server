var gulp = require('gulp');
var babel = require('gulp-babel');
var sourcemaps = require('gulp-sourcemaps');
var jshint = require('gulp-jshint');
var run = require('gulp-run');

gulp.task('babel', function () {
    return gulp.src('./migration/src/**/*.js')
        .pipe(sourcemaps.init())
        .pipe(babel())
        .pipe(sourcemaps.write('./', {
            sourceRoot: '../src'
        }))
        .pipe(gulp.dest('./migration/dist'));
});

gulp.task('lint', function () {
    return gulp.src('./migration/dist/**/*.js')
        .pipe(jshint());
});

gulp.task('remove-uploads', function () {
    return run('rm -rf /data/*').exec();
});

gulp.task('db-before', function () {
    return run('mysql -uindiepost -pindiepost indiepost < ./migration/migration-0.sql').exec();
});

gulp.task('db-after', function () {
    return run('mysql -uindiepost -pindiepost indiepost < ./migration/migration-1.sql').exec();
});

gulp.task('prepare', ['remove-uploads', 'db-before', 'babel', 'lint'], function () {});

gulp.task('migrate0', ['prepare'], function () {
    return run('node ./migration/dist/migration-0.js > ./migration.log').exec();
});

gulp.task('migrate1', ['babel', 'lint', 'db-after'], function () {
    return run('node ./migration/dist/migration-1.js > ./migration.log').exec();
});

gulp.task('watch', function () {
    gulp.watch('./migration/src/**/*.js', ['babel', 'lint'], function (e) {
        console.log('Event type: ' + e.type);
        console.log('Event name: ' + e.name);
    });
});