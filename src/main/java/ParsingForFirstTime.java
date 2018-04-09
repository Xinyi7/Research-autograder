

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.StringProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LiteralExpr;
import com.github.javaparser.ast.expr.LiteralStringValueExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.TreeVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.printer.SourcePrinter;
import com.github.javaparser.printer.XmlPrinter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import com.github.javaparser.utils.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParsingForFirstTime {
    private static String[][] arrayOfIndex;
    private static final String File_Path = "C:/Users/Elizabeth Jiang/workspace/newproject/src/newproject/Test1.java";
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        new String("public class ForToWhile {\r\n" +
                "\r\n" +
                "	   public static void main(String[] args) {\r\n" +
                "	        int a = 2;\r\n" +
                "	        int b = 4;\r\n" +
                "	        for (int i = a; i < b; i++) {\r\n" +
                "	           int c = i + 1;\r\n" +
                "	           System.out.println(\"hey \" + i);\r\n" +
                "	           for (int j = c; j > 0; j--) {\r\n" +
                "	               System.out.println(\"you \" + j);\r\n" +
                "	           }\r\n" +
                "	        }\r\n" +
                "	    }\r\n" +
                "	}");
        // Parse an expression
        Expression expressionNode = JavaParser.parseExpression("1 + 2");

        // Parse a body declaration: it could be either a field or a method or an inner class
        BodyDeclaration methodNode = JavaParser.parseClassBodyDeclaration(
                "boolean invert(boolean aFlag) { return !p; }");

        // Parse the code of an entire source file, a.k.a. a Compilation Unit
        //CompilationUnit compilationUnitNode = JavaParser.parse(example);


        CompilationUnit cu=JavaParser.parse(new FileInputStream(File_Path));
        //CompilationUnit cu2=JavaParser.parse("C:/Users/Elizabeth Jiang/workspace/newproject/src/newproject/DirExplorer.java");
        System.out.println(cu);
        List<Node> list= cu.getChildNodes();
        System.out.print(list);
        for (int i = 0 ; i <list.size();i++){
            list.get(i);
        }
        cu.addClass("New Class");
        System.out.println(cu);



        Pair<ParseResult<CompilationUnit>, LexicalPreservingPrinter> result = LexicalPreservingPrinter.setup(
                ParseStart.COMPILATION_UNIT, new StreamProvider(new FileInputStream(File_Path)));

        LexicalPreservingPrinter lpp = result.b;
        //System.out.println(lpp.print(cu2));


        // If we know the expression is a binary expression we can cast it and access more
        // specific information like the element on the left and on the right and the operator
        BinaryExpr binaryExpr = (BinaryExpr)expressionNode;
        System.out.println(String.format("Binary expression: left=%s, right=%s, operator=%s",
                binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator()));

        // Here we know we have a method declaration. We may want to figure out specific
        // things like the name or the return type of the method.
        // We transform the parameters to get only the names: we are not interested in
        // in printing the whole nodes corresponding to the parameters
        MethodDeclaration methodDeclaration = (MethodDeclaration)methodNode;
        System.out.println(String.format("Method declaration: modifiers=%s, name=%s, parameters=%s, returnType=%s",
                methodDeclaration.getModifiers(), methodDeclaration.getName(),
                methodDeclaration.getParameters().stream().map(p -> p.getName()).collect(Collectors.toList()),
                methodDeclaration.getType()));

        // We can navigate the compilation unit to extract a single class. In this case
        // the CompilationUnit contains only this class but in general it could contains
        // a package declaration, imports and several type declarations
        //store varibles in an array
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName("ForToWhile").get();
        System.out.println(String.format("Class declaration: name=%s, nMembers=%s",
                classDeclaration.getName(), classDeclaration.getMembers().size()));
        classDeclaration.setName("NewForToWhile");
        System.out.println(cu.getChildNodesByType(VariableDeclarator.class));
        List<VariableDeclarator> listOfVariable = cu.getChildNodesByType(VariableDeclarator.class);
        arrayOfIndex = new String[listOfVariable.size()][2];
        for (int i = 0 ; i < listOfVariable.size();i++){
        	arrayOfIndex[i][0]=listOfVariable.get(i).getNameAsString();
        	listOfVariable.get(i).setName("v"+i);
        	arrayOfIndex[i][1]="v"+i;
        }

        //replace all variables



       for (SimpleName v:cu.getChildNodesByType(SimpleName.class)){
    	   System.out.println(v);
    	   for (int i = 0 ; i < arrayOfIndex.length;i++){

    		   v.setIdentifier(v.getIdentifier().replaceAll("\\b"+arrayOfIndex[i][0]+"\\b",arrayOfIndex[i][1]));
    	   }

       }
        //ModifierVisitor<?> variableModifier = new VariableModifier();

        //variableModifier.visit(cu, null);*/

        //System.out.println(listOfVariable);
        System.out.println(cu.toString());
        System.out.println("------------------------------------");

        PrintOutTree a = new PrintOutTree(false);
        System.out.print(a.output(cu));

        System.out.println("------------------------------------");
    }



}





