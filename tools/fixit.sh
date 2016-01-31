perl -i -pe 's/\r//g;s/[\t ]+$//' $(find -name *.java) TODO.txt build.gradle
