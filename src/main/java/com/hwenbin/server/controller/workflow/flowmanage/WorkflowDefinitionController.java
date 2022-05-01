package com.hwenbin.server.controller.workflow.flowmanage;

import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.flow.WorkflowDefinitionDTO;
import com.hwenbin.server.dto.flow.WorkflowDesignerDTO;
import com.hwenbin.server.service.WorkflowDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 流程定义控制器
 *
 * @author hwb
 * @date 2022/04/30 19:54
 */
@Slf4j
@RestController
@RequestMapping("/workflow/definition")
public class WorkflowDefinitionController {

    @Resource
    private WorkflowDefinitionService flowDefinitionService;

    /**
     * 流程定义列表
     * @param pageParam
     * @return
     */
    @GetMapping(value = "/list")
    public CommonResult<PageResult<WorkflowDefinitionDTO>> list(PageParam pageParam) {
        return ResultGenerator.genOkResult(flowDefinitionService.list(pageParam));
    }

    /**
     * 列出指定流程的发布版本列表
     *
     * @param processKey 流程定义Key
     * @return
     */
    @GetMapping(value = "/publishList")
    public CommonResult<PageResult<WorkflowDefinitionDTO>> publishList(@RequestParam String processKey,
                                                         PageParam pageParam) {
        return ResultGenerator.genOkResult(flowDefinitionService.publishList(processKey, pageParam));
    }


    /**
     * 导入流程文件
     * notes：上传bpmn20的xml文件
     * @param name
     * @param category
     * @param file
     * @return
     */
    @PostMapping("/import")
    public CommonResult<String> importFile(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String category,
                              MultipartFile file) {
        try (InputStream in = file.getInputStream()) {
            flowDefinitionService.importFile(name, category, in);
        } catch (Exception e) {
            log.error("导入失败:", e);
            return ResultGenerator.genFailedResult("导入失败：" + e.getMessage());
        }

        log.info("导入成功");
        return ResultGenerator.genOkResult("导入成功");
    }


    /**
     * 读取xml文件
     * @param definitionId 流程定义ID
     * @return
     */
    @GetMapping("/readXml/{definitionId}")
    public CommonResult<String> readXml(@PathVariable(value = "definitionId") String definitionId) {
        try {
            return ResultGenerator.genOkResult(flowDefinitionService.readXml(definitionId));
        } catch (Exception e) {
            return ResultGenerator.genFailedResult("加载xml文件异常");
        }

    }

    /**
     * 读取图片文件
     * @param definitionId 流程定义id
     * @param response
     */
    @GetMapping("/readImage/{definitionId}")
    public void readImage(@PathVariable(value = "definitionId") String definitionId,
                          HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()) {
            BufferedImage image = ImageIO.read(flowDefinitionService.readImage(definitionId));
            response.setContentType("image/png");
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存流程设计器内的xml文件
     * @param dto
     * @return
     */
    @PostMapping("/save")
    public CommonResult<String> saveDesigner(@RequestBody WorkflowDesignerDTO dto) {
        try (InputStream in = new ByteArrayInputStream(dto.getXml().getBytes(StandardCharsets.UTF_8))) {
            flowDefinitionService.importFile(dto.getName(), dto.getCategory(), in);
        } catch (Exception e) {
            log.error("导入失败:", e);
            return ResultGenerator.genFailedResult("导入失败：" + e.getMessage());
        }

        log.info("导入成功");
        return ResultGenerator.genOkResult("导入成功");
    }

    /**
     * 激活或挂起流程定义
     * @param suspended ture:挂起,false:激活
     * @param definitionId 流程定义ID
     * @return
     */
    @PutMapping(value = "/updateState")
    public CommonResult<Void> updateState(@NotNull(message = "状态不能为空") @RequestParam Boolean suspended,
                               @NotBlank(message = "流程定义ID不能为空") @RequestParam String definitionId) {
        flowDefinitionService.updateState(suspended, definitionId);
        return ResultGenerator.genOkResult();
    }

    /**
     * 删除流程
     * @param deployId
     * @return
     */
    @DeleteMapping(value = "/delete")
    public CommonResult<Void> delete(@NotBlank(message = "流程部署ID不能为空") @RequestParam String deployId) {
        flowDefinitionService.delete(deployId);
        return ResultGenerator.genOkResult();
    }

}
