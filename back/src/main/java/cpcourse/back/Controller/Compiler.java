package cpcourse.back.Controller;

import com.alibaba.fastjson.JSON;
import cpcourse.back.Service.LexicalAnalysis;
import cpcourse.back.Util.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/compiler")
public class Compiler {
    @Autowired
    LexicalAnalysis lexicalAnalysis;

    @RequestMapping("/LexicalAnalysis")
    ResponseEntity lexicalAnalysis(@RequestBody String json){
        Map maps = (Map) JSON.parse(json);
        String code = maps.get("code").toString();
        lexicalAnalysis.lexicalAnalysis(code);
        return null;
    }
}
