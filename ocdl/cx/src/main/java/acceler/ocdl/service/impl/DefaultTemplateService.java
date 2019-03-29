package acceler.ocdl.service.impl;

import acceler.ocdl.persistence.ProjectCrud;
import acceler.ocdl.service.TemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultTemplateService implements TemplateService {

    @Autowired
    private ProjectCrud projectCrud;

    @Override
    public List<String> getTemplatesList(String type) {

        List<String> templatesList = getFile(projectCrud.getProjectConfiguration().getTemplatePath()+type);
        return templatesList;
    }


    /*
     * 函数名：getFile
     * 作用：使用递归，输出指定文件夹内的所有文件
     * 参数：path：文件夹路径
     */
    private static List<String> getFile(String path) {
        // 获得指定文件对象
        List<String> nameList = new ArrayList<String>();
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile())//如果是文件
            {

                nameList.add(array[i].getName());
            }
        }
        return nameList;
    }

    @Override
    public List<String> getTemplates2(String name,String type) {
        List<String> result = new ArrayList<>();
        //FIXME: StringBuilder
        String code = "";
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

            /* 读入TXT文件 */
            String pathname = projectCrud.getProjectConfiguration().getTemplatePath()+type+"//"+ name; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while (line != null && !line.equals("null")) {
                code += "\n";
                line = br.readLine(); // 一次读入一行数据
                code += line;
            }
        } catch (Exception e) {
        }
        result.add(code);
        result.add("I'm the description");


        return result;
    }


}
