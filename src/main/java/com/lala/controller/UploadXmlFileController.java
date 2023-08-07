package com.lala.controller;


import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("")
public class UploadXmlFileController {
    @ApiOperation(value = "查单", notes = "查单")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multiFile) throws IOException {

      

        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        File file = File.createTempFile(fileName, prefix);
        multiFile.transferTo(file);
        boolean isValid = validateXml(file);
        if (!isValid) {
            throw new RuntimeException("Invalid XML file. Please upload a valid XML.") ;
        }
        String xml = new String(multiFile.getBytes(), StandardCharsets.UTF_8);
        JSONObject json = JSONUtil.parseFromXml(xml);


        return ResponseEntity.ok(json);
    }
    private boolean validateXml(File file) {
        try {
            //创建默认的XML错误处理器
            XMLErrorHandler errorHandler = new XMLErrorHandler();
            //获取基于 SAX 的解析器的实例
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //解析器在解析时验证 XML 内容。
            factory.setValidating(true);
            //指定由此代码生成的解析器将提供对 XML 名称空间的支持。
            factory.setNamespaceAware(true);
            //使用当前配置的工厂参数创建 SAXParser 的一个新实例。
            SAXParser parser = factory.newSAXParser();
            //创建一个读取工具
            SAXReader xmlReader = new SAXReader();
            //获取要校验xml文档实例
            Document xmlDocument = xmlReader.read(file);
            //设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在 [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaSource", "file:" + getXsdSchema());
            //创建一个SAXValidator校验工具，并设置校验工具的属性
            SAXValidator validator = new SAXValidator(parser.getXMLReader());
            //设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
            validator.setErrorHandler(errorHandler);
            //校验
            validator.validate(xmlDocument);

//			XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            //如果错误信息不为空，说明校验失败，打印错误信息
            if (errorHandler.getErrors().hasContent()) {
                Element e = errorHandler.getErrors();
                List<Node> list = e.content();
                int count = 0;
                String result = "";
                for (Node node : list) {
                    String error = node.getText().split(":")[1];
                    if (error.contains("元素") && error.contains("无效")) {
                        count++;
                        result += error + "\n";
                    }
                }
                System.err.println("共有" + count + "处错误");
                System.err.println(result);
                return false;
            } else {
                System.err.println("Good! XML文件通过XSD文件校验成功！");
                return true;
            }
        } catch (Exception ex) {
            System.err.println("XML文件: " + file + " 通过XSD文件:" + getXsdSchema() + "检验失败。\n原因： " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;

    }
    private String getXsdSchema() {
        return "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xs:element name=\"BCO_01\">\n" +
                "        <xs:complexType>\n" +
                "            <xs:sequence>\n" +
                "                <xs:element name=\"serial_no\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"30\" />\n" +
                "                <xs:element name=\"healthcare_staff_english_title\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_english_given_name\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_english_surname\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"applicant_gender\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"30\" />\n" +
                "                <xs:element name=\"applicant_date_of_birth\" type=\"xs:date\" />\n" +
                "                <xs:element name=\"applicant_identity_document_number\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_eng_block\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_eng_building\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_eng_estate\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_eng_floor\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_eng_room\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_eng_street_number\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_subdistrict\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_district\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_region\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_state\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_country_territory\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_postal_code\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_address_csuid\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"healthcare_staff_foreign_address_indicator\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"30\" />\n" +
                "                <xs:element name=\"license_number\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"30\" />\n" +
                "                <xs:element name=\"registration_date\" type=\"xs:date\" />\n" +
                "                <xs:element name=\"qualifications\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "                <xs:element name=\"license_issue_date\" type=\"xs:date\" />\n" +
                "                <xs:element name=\"notice\" type=\"xs:string\" minOccurs=\"0\" maxLength=\"50\" />\n" +
                "            </xs:sequence>\n" +
                "        </xs:complexType>\n" +
                "    </xs:element>\n" +
                "</xs:schema>";
    }

}
