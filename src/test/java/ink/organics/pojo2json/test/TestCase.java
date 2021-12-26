package ink.organics.pojo2json.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.annotation.Annotation;

public abstract class TestCase extends BasePlatformTestCase {


    private final ObjectMapper objectMapper = new ObjectMapper();

    public TestCase() {
        // https://github.com/FasterXML/jackson-databind/issues/2087
        this.objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        this.objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        this.objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected boolean annotatedWith(@NotNull Class<? extends Annotation> annotationClass) {
        return super.annotatedWith(annotationClass);
    }

    @Override
    protected abstract String getTestDataPath();

    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new LightProjectDescriptor() {
            @Override
            public Sdk getSdk() {
                return JavaAwareProjectJdkTableImpl.getInstanceEx().getInternalJdk();
            }
        };
    }

    public JsonNode testAction(@NotNull String fileName, @NotNull AnAction action) {

        // Open file and simulate user cursor position to class scope.
        myFixture.configureByFile(fileName);
        PsiElement psiElement = myFixture.findElementByText("class", PsiElement.class);
        int offset = psiElement.getTextOffset();
        myFixture.getEditor().getCaretModel().moveToOffset(offset);

        myFixture.testAction(action);

        Transferable result = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        try {
            String jsonStr = String.valueOf(result.getTransferData(DataFlavor.stringFlavor));
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            System.out.println(jsonNode.toPrettyString());
            return jsonNode;
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}