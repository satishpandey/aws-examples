package com.cloudtechpro.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JavaStreamExample {

    public static void main(String[] args) {
	JavaStreamExample streamExample = new JavaStreamExample();
	System.out.println("---------- Iterate all items ---------------------");
	streamExample.displayAll(streamExample.getStreamOfArray());
	System.out.println("---------- Iterate filtered items ----------------");
	streamExample.displayAllContainsChar(streamExample.getStreamOfCollection(), "n");
	System.out.println("---------- Showing distinct elements -------------");
	streamExample.displayDistinctItems(streamExample.getStreamUsingBuilder());
	System.out.println("---------- Showing flat map transformation -------");
	streamExample.displayDistinctItems(streamExample.getStreamOfWords());
	System.out.println("---------- Showing map transformation ------------");
	streamExample.displayDistinctItems(streamExample.getStreamOfUpperCaseWords(streamExample.getStreamOfWords()));
	System.out.println("---------- Showing Combined results --------------");
	System.out.println(streamExample.getReducedString(streamExample.getStreamOfCollection()));
    }

    public void displayAll(Stream<String> stream) {
	stream.forEach(item -> System.out.println(item));
    }

    public void displayAllContainsChar(Stream<String> stream, CharSequence charSequence) {
	stream.filter(item -> item.contains(charSequence)).forEach(item -> System.out.println(item));
    }

    public void displayDistinctItems(Stream<String> stream) {
	stream.distinct().forEach(item -> System.out.println(item));
    }

    public Stream<String> getStreamOfUpperCaseWords(Stream<String> stream) {
	return stream.map(word -> word.toUpperCase());
    }

    public Stream<String> getStreamOfWords() {
	Collection<String> lines = Arrays.asList("First line of Array", "Second line of Array");
	return lines.stream().flatMap(line -> Arrays.stream(line.split("\\s")));
    }

    public String getReducedString(Stream<String> stream) {
	return stream.reduce((item1, item2) -> item1 + item2).get();
    }

    public Stream<String> getStreamOfArray() {
	return Stream.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    }

    public Stream<String> getStreamOfCollection() {
	Collection<String> weekDays = Arrays.asList(new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" });
	return weekDays.stream();
    }

    public Stream<?> stream(List<?> list) {
	if (list == null || list.isEmpty()) {
	    return Stream.empty();
	}
	return list.stream();
    }

    public Stream<String> getStreamUsingBuilder() {
	return Stream.<String>builder().add("Sunday").add("Monday").add("Tuesday").add("Sunday").add("Monday").add("Tuesday").build();
    }

    public Stream<String> getStreamUsingString() {
	return Pattern.compile(",").splitAsStream("Sunday, Monday, Tuesday");
    }

    public Stream<String> getStreamOfFile(Path path) throws IOException {
	return Files.lines(path);
    }

}
