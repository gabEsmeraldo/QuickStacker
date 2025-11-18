package com.QuickStacker.back;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackApplication {

	public static void main(String[] args) {
		// Load .env file - try multiple locations
		Dotenv dotenv = null;
		String currentDir = System.getProperty("user.dir");
		String parentDir = currentDir != null ? new java.io.File(currentDir).getParent() : null;
		
		// Try multiple locations in order of preference
		String[] pathsToTry = {
			parentDir != null ? parentDir : "..",  // Parent directory (when running from back/)
			currentDir,                              // Current directory
			".",                                     // Current directory (relative)
			".."                                     // Parent directory (relative)
		};
		
		for (String path : pathsToTry) {
			try {
				java.io.File envFile = new java.io.File(path, ".env");
				if (envFile.exists() && envFile.isFile()) {
					dotenv = Dotenv.configure()
						.directory(path)
						.ignoreIfMissing()
						.load();
					System.out.println("Loaded .env file from: " + envFile.getAbsolutePath());
					break;
				}
			} catch (Exception e) {
				// Continue trying next path
				continue;
			}
		}
		
		// If still not found, try without specifying directory (uses current working directory)
		if (dotenv == null) {
			try {
				dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();
				if (dotenv != null) {
					System.out.println("Loaded .env file from current working directory");
				}
			} catch (Exception e) {
				// .env file not found
			}
		}
		
		// Set system properties from .env file if loaded
		if (dotenv != null) {
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
			System.out.println("Loaded .env file successfully with " + dotenv.entries().size() + " variables");
		} else {
			System.out.println("Warning: .env file not found. Using system environment variables or defaults.");
			System.out.println("Current working directory: " + currentDir);
			System.out.println("Tried paths: " + java.util.Arrays.toString(pathsToTry));
		}
		
		SpringApplication.run(BackApplication.class, args);
	}

}
