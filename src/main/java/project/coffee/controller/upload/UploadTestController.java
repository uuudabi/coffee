package project.coffee.controller.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.coffee.domain.dto.upload.CoffeeDTO;
import project.coffee.domain.dto.upload.CoffeeImageDTO;
import project.coffee.domain.entity.CoffeeImage;
import project.coffee.domain.entity.CoffeeList;
import project.coffee.repository.upload.CoffeeImageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class UploadTestController {

    @Autowired
    CoffeeImageRepository coffeeImageRepository;

    @GetMapping("/uploadEx")
    public void uploadEx() {

    }

    @GetMapping("/coffeeRegister")
    public void register(){

    }

    @PostMapping("/register")
    public String register(CoffeeDTO coffeeDTO, RedirectAttributes redirectAttributes){
        log.info("movieDTO: " + coffeeDTO);

        //추후 service로 만들어야 함
        Map<String, Object> entityMap = dtoToEntity(coffeeDTO);
        CoffeeList coffee = (CoffeeList) entityMap.get("movie");
        List<CoffeeImage> movieImageList = (List<CoffeeImage>) entityMap.get("imgList");

        movieImageList.forEach(coffeeImage -> {
            coffeeImageRepository.save(coffeeImage);
        });

        return "redirect:/movie/list";
    }

    private Map<String, Object> dtoToEntity(CoffeeDTO coffeeDTO){

        Map<String, Object> entityMap = new HashMap<>();

        CoffeeList movie = CoffeeList.builder()
                .title(coffeeDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<CoffeeImageDTO> imageDTOList = coffeeDTO.getImageDTOList();

        if(imageDTOList != null && imageDTOList.size() > 0 ) { //MovieImageDTO 처리

            List<CoffeeImage> coffeeImageList = imageDTOList.stream().map(coffeeImageDTO ->{

                CoffeeImage coffeeImage = CoffeeImage.builder()
                        .path(coffeeImageDTO.getPath())
                        .imgName(coffeeImageDTO.getImgName())
                        .uuid(coffeeImageDTO.getUuid())
                        .build();
                return coffeeImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", coffeeImageList);
        }

        return entityMap;
    }
}
