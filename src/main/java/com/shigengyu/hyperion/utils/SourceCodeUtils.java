package com.shigengyu.hyperion.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import com.shigengyu.hyperion.HyperionException;
import com.shigengyu.hyperion.core.WorkflowInstance;

public class SourceCodeUtils {

	private static String getSourceCodeFromFile(final File sourceFile) {
		String sourceFileContent;
		try {
			sourceFileContent = Files.toString(sourceFile, Charset.forName("UTF-8"));
		}
		catch (IOException e) {
			throw new HyperionException("Failed to get source code from file [" + sourceFile.getAbsolutePath() + "]", e);
		}
		return sourceFileContent;
	}

	public static String getSourceCodeInEclipse(final Class<?> clazz, String... sourceCodePathElements) {

		if (sourceCodePathElements == null || sourceCodePathElements.length == 0) {
			sourceCodePathElements = new String[] { "src", "main", "java" };
		}

		String classFilePath = WorkflowInstance.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		StringBuilder sb = new StringBuilder(new File(classFilePath).getParentFile().getParentFile().getAbsolutePath());

		for (final String pathElement : sourceCodePathElements) {
			sb.append(File.separatorChar + pathElement);
		}

		String sourceDirectory = sb.toString();
		String fileRelativePath = clazz.getPackage().getName().replaceAll("\\.", "/");
		File sourceFile = new File(sourceDirectory + File.separatorChar + fileRelativePath, clazz.getSimpleName()
				+ ".java");

		return getSourceCodeFromFile(sourceFile);
	}
}
