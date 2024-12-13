/*sudo apt install python3
 * sudo apt install g++
 * sudo apt install openjdk-21-jdk
 * 
 * 
 * ubuntu use python3 , window use python change as per needed
 */

package com.example.linklocal.Controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompileController {

    @PostMapping("/compile")
    @ResponseBody
    public Map<String, String> compile(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String input = request.get("input");
        String lang = request.get("lang");

        Map<String, String> response = new HashMap<>();

        try {
            if ("cpp".equalsIgnoreCase(lang)) {
                response.put("output", compileCpp(code, input));
            } else if ("java".equalsIgnoreCase(lang)) {
                response.put("output", compileJava(code, input));
            } else if ("python".equalsIgnoreCase(lang)) {
                response.put("output", compilePython(code, input));
            } else {
                response.put("output", "Unsupported language");
            }
        } catch (Exception e) {
            response.put("output", "Error during compilation: " + e.getMessage());
        }

        return response;
    }

    private String compileCpp(String code, String input) throws Exception {
        File sourceFile = File.createTempFile("Main", ".cpp");
        File executableFile = new File(sourceFile.getParent(), "Main.exe");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            writer.write(code);
        }

        Process compileProcess = new ProcessBuilder("g++", sourceFile.getAbsolutePath(), "-o", executableFile.getAbsolutePath()).start();
        if (compileProcess.waitFor() != 0) {
            return new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()))
                .lines()
                .reduce("", String::concat);
        }

        Process runProcess = new ProcessBuilder(executableFile.getAbsolutePath()).start();
        if (input != null && !input.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                writer.write(input);
            }
        }

        if (runProcess.waitFor() != 0) {
            return new BufferedReader(new InputStreamReader(runProcess.getErrorStream()))
                .lines()
                .reduce("", String::concat);
        }

        return new BufferedReader(new InputStreamReader(runProcess.getInputStream()))
            .lines()
            .reduce("", String::concat);
    }

    private String compileJava(String code, String input) throws Exception {
        File sourceFile = new File(System.getProperty("java.io.tmpdir"), "Main.java");


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            writer.write(code);
        }

        Process compileProcess = new ProcessBuilder("javac", sourceFile.getAbsolutePath()).start();
        if (compileProcess.waitFor() != 0) {
            return new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()))
                .lines()
                .reduce("", String::concat);
        }

        Process runProcess = new ProcessBuilder("java", "-cp", sourceFile.getParent(), "Main").start();
        if (input != null && !input.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                writer.write(input);
            }
        }

        if (runProcess.waitFor() != 0) {
            return new BufferedReader(new InputStreamReader(runProcess.getErrorStream()))
                .lines()
                .reduce("", String::concat);
        }

        return new BufferedReader(new InputStreamReader(runProcess.getInputStream()))
            .lines()
            .reduce("", String::concat);
    }

    private String compilePython(String code, String input) throws Exception {
        File sourceFile = File.createTempFile("Main", ".py");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            writer.write(code);
        }

        Process runProcess = new ProcessBuilder("python3", sourceFile.getAbsolutePath()).start();
        if (input != null && !input.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                writer.write(input);
            }
        }

        if (runProcess.waitFor() != 0) {
            return new BufferedReader(new InputStreamReader(runProcess.getErrorStream()))
                .lines()
                .reduce("", String::concat);
        }

        return new BufferedReader(new InputStreamReader(runProcess.getInputStream()))
            .lines()
            .reduce("", String::concat);
    }
}
