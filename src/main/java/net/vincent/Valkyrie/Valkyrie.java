package net.vincent.Valkyrie;

import net.vincent.Valkyrie.Exceptions.*;
import net.vincent.Valkyrie.Exceptions.Exception;

import java.io.*;
import java.util.Arrays;

public class Valkyrie {

    private static boolean evaluateFile(String file) throws FileExtensionIncorrectException {

        if(file.endsWith(".val"))
            return true;
        else {
            throw new FileExtensionIncorrectException();
        }

    }

    public Valkyrie(String inputFile) throws UnkownStarterException {

        Define define = new Define();
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFile))) {
            BufferedWriter out = new BufferedWriter(new FileWriter("./temp.asm"));
            String line;
            int lineNum = 0;
            while ((line = inputFileReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] tokens = line.split(" ");
                    switch (tokens[0]) {
                        case "#@": define.defInt(out, tokens, tokens[0]); break;
                        case "#!": define.defFloat(out, tokens, tokens[0]); break;
                        case "##": define.defDouble(out, tokens, tokens[0]); break;
                        case "#$": define.defBool(out, tokens, tokens[0]); break;
                        case "#^": define.defChar(out, tokens, tokens[0]); break;
                        default: throw new UnkownStarterException(tokens[0], lineNum);
                    }
                }
                lineNum++;
            }
            out.close(); // finish writing data section

            // ✅ Post-generation check
            String asmContent = new String(
                    java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("./temp.asm"))
            );

            // Only append if both section and label are missing
            if (!asmContent.contains("__TEXT,__text") && !asmContent.contains("_main")) {
                try (BufferedWriter asmOut = new BufferedWriter(new FileWriter("./temp.asm", true))) {
                    asmOut.write("\n// --- Code section ---\n");
                    asmOut.write("\t.section\t__TEXT,__text\n");
                    asmOut.write("\t.global\t_main\n");
                    asmOut.write("\t.align\t2\n\n");
                    asmOut.write("_main:\n");
                    asmOut.write("\t// For now, just exit(0)\n");
                    asmOut.write("\tmov\tx0, #0          // return code\n");
                    asmOut.write("\tmov\tx16, #1         // macOS syscall: exit\n");
                    asmOut.write("\tsvc\t#0\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }catch (IllegalFormatException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public static void main(String[] args)throws NoInputFileException {
        if(args.length != 0) {
            try {
                if (evaluateFile(args[0]))
                    new Valkyrie(args[0]);
            } catch (FileExtensionIncorrectException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            } catch(UnkownStarterException e){
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }else{
            throw new NoInputFileException();
        }
    }

}
