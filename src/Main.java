import com.compiler.action.CreateAutomaton;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String input;
        CreateAutomaton CreateAutomaton = new CreateAutomaton();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter regular expression : ");
        input = scanner.nextLine();
        if (CreateAutomaton.isValid(input)){
            CreateAutomaton.startProcess(input);
        }
    }
}
