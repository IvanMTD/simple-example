package task.simpleexample;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import task.simpleexample.controller.MainController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMethodCheck_AlwaysRedirect() throws Exception {
        String symbolSuccess = "RUB";
        String symbolFail = "ZZZ";
        String symbolRandom = "123abcABC";
        String symbolEmpty = "";

        mockMvc.perform(get("/check").param("symbol",symbolSuccess)).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/check").param("symbol",symbolFail)).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/check").param("symbol",symbolRandom)).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/check").param("symbol", symbolEmpty)).andExpect(status().is3xxRedirection());
    }
}
