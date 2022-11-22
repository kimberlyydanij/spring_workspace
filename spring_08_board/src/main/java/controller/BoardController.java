package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dto.BoardDTO;
import dto.PageDTO;
import service.BoardService;


// http://localhost:8090/myapp/list.sb

@Controller
public class BoardController {

	private BoardService service;
	private PageDTO pdto;
	private int currentPage;
	
	public BoardController() {
		
	}
	
	public void setService(BoardService service) {
		this.service = service;
	}
	
	@RequestMapping("/list.sb")
	public ModelAndView listMethod(PageDTO pv, ModelAndView mav) {
		int totalRecord = service.countProcess();
		if(totalRecord>=1) {
			if(pv.getCurrentPage() ==0 ) 
				this.currentPage = 1;
		else 
			this.currentPage = pv.getCurrentPage();
			
			this.pdto = new PageDTO(this.currentPage, totalRecord);
			List<BoardDTO> aList = service.listProcess(this.pdto);
			mav.addObject("aList",aList);
			mav.addObject("pv", this.pdto);		
		} // end if
		
		mav.setViewName("board/list");
		return mav;
	} // end listMethod();

	@RequestMapping(value="/write.sb", method=RequestMethod.GET)
	public ModelAndView writeMethod(BoardDTO dto, PageDTO pv, ModelAndView mav) {
			if(dto.getRef()!=0) { // 답변 글이면?
				 mav.addObject("currentPage", pv.getCurrentPage());
				 mav.addObject("dto",dto);
			}  //  제목글이면
				mav.setViewName("board/write");
		return mav;
	} // end writeMethod
	
	@RequestMapping(value="/write.sb", method=RequestMethod.POST)
	public String writeProMethod(BoardDTO dto, PageDTO pv, HttpServletRequest request) {
		
		MultipartFile file = dto.getFilename();
		if(!file.isEmpty()) {
			UUID random = saveCopyFile(file, request);	
			dto.setUpload(random+"_"+file.getOriginalFilename());
		}
		dto.setIp(request.getRemoteAddr());
		
		service.insertProcess(dto);
		
		if(dto.getRef() != 0 ) {
			return "redirect:/list.sb?currentPage=" + pv.getCurrentPage();
		} else {
			return "redirect:/list.sb";
		}
		
	} // end writeProMethod
	
	private UUID saveCopyFile(MultipartFile file, HttpServletRequest request) {
		String fileName = file.getOriginalFilename();
		
		//중복파일명을 처리하기 위해 난수 발생
		 UUID random = UUID.randomUUID();
		 
		 File fe = new File(urlPath(request));
		 if(!fe.exists()) {
			 fe.mkdir();
		 }
		 File ff = new File(urlPath(request), random + "_" + fileName); 
		 
		 try {
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(ff));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 
		return random;
	} // end saveCopyFile()
	
	private String urlPath(HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("/");
		System.out.println("root : "+root);
		String saveDirectory = root + "temp" + File.separator;
		return saveDirectory;
	} // end urlPath
	
	@RequestMapping("/view.sb")
	public ModelAndView viewMethod(int currentPage, int num, ModelAndView mav) {
		mav.addObject("dto", service.contentProcess(num));
		mav.addObject("currentPage", currentPage);
		mav.setViewName("board/view");
		return mav;
	} // end viewMethod();
	
	@RequestMapping("/contentdownload.sb")
	public ModelAndView downMethod(int num, ModelAndView mav) { 
		mav.addObject("num", num);
		mav.setViewName("download");
		return mav;
	} // end downMethod();
	
} // end class
