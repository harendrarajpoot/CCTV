package com.app.serviceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.constants.Message;
import com.app.exception.ExceptionUtils;
import com.app.jwt.JwtFilter;
import com.app.modal.Bill;
import com.app.repository.BillRepository;
import com.app.service.BillService;
import com.app.util.Utils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private JwtFilter jwtFilter;

	

	@Override
	public ResponseEntity<String> generateBill(Map<String, String> requestMap) {
		try {
			String fileName = null;
			if (validateRequestMap(requestMap)) {
				if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.containsKey("isGenerate")) {

					fileName = requestMap.get("uuid");
				} else {

					fileName = Utils.getUUID();
					requestMap.put("uuid", fileName);
					saveBill(requestMap);
				}
				// generate PDF

				getneratePDF(requestMap,fileName);
			} else {
				return ExceptionUtils.getReponseEntity(Message.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<String> getneratePDF(Map<String, String> requestMap,String fileName) {
		String data = "Name: " + requestMap.get("name") + "\n" + "ContactNumber: " + requestMap.get("contactNumber")
				+ "\n" + "Email: " + requestMap.get("email") + "\n" + "Payment Method:"
				+ requestMap.get("paymentMethod");
		
			try {
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(Message.STORE_LOCATION+"\\"+fileName+".pdf"));
				document.open();
				setRectangleInPDF(document);
				Paragraph chunk=new Paragraph("CCTV Management System",getFont("Header"));
				
				chunk.setAlignment(Element.ALIGN_CENTER);
				document.add(chunk);
				
				Paragraph paragraph=new Paragraph(data+"\n \n",getFont(data));
				
				document.add(paragraph);
				
				PdfPTable table=new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);
				
				
			JSONArray jsonArray=Utils.getJsonArrayFromString(requestMap.get("productDetails"));
			
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					addRows(table,Utils.getMapFromJson(jsonArray.getString(i)));
					System.out.println("00000000000000000000000");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("11111111111111");
			
			document.add(table);
			
			Paragraph footer =new Paragraph("Total: "+requestMap.get("totalAmount")+"\n"+"Thank you for visiting Please visit again!!",getFont(data));
				document.add(footer);
				document.close();
				
				return new ResponseEntity<>("{\"message\":\"" + fileName + "\"}", HttpStatus.OK);
			} catch (FileNotFoundException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ExceptionUtils.getReponseEntity(Message.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void addRows(PdfPTable table, Map<String, String> data) {
		log.info("addRows-------------");
		
		table.addCell(data.get("name"));
		table.addCell(data.get("category"));
		table.addCell(data.get("quantity"));
		Double price=(Double.parseDouble(data.get("price")));
		System.out.println("22444444444444442222222222222222222222222222");
		table.addCell(Double.toString(price));
		System.out.println("11111111111111111111111222222222222222222222222222222");
		table.addCell(data.get("total"));
		System.out.println("222222222222222222222222222222");
		
	}

	private void addTableHeader(PdfPTable table) {
		log.info("Inside addTableHeader ");
		Stream.of("Name","Category","Quantity","Price","Sub Total").
		forEach(columnTitle->{
			PdfPCell header=new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
		});
		
	}

	private Font getFont(String type) {
		log.info("inside font...");
		
		switch (type) {
		case "Header":
			Font headerFont=FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;

		case "data":
			Font dataFont=FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;
		default :
		return new Font();
		}
	}

	private void setRectangleInPDF(Document document) {
		log.info("setRectangleInPDF");
		Rectangle rectangle=new Rectangle(577, 825, 18, 15);
			
			rectangle.enableBorderSide(1);
			rectangle.enableBorderSide(2);
			rectangle.enableBorderSide(4);
			rectangle.enableBorderSide(8);
			rectangle.setBackgroundColor(BaseColor.BLACK);
			rectangle.setBorderWidth(1);
			try {
				document.add(rectangle);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void saveBill(Map<String, String> requestMap) {

		Bill bill = new Bill();
		bill.setUuid(requestMap.get("uuid"));
		bill.setName(requestMap.get("name"));
		bill.setEmail(requestMap.get("email"));
		bill.setContact(requestMap.get("contactNumber"));
		bill.setPaymentMethod(requestMap.get("paymentMethod"));
		bill.setTotal((Integer.parseInt(requestMap.get("totalAmount"))));
		bill.setProductDetails(requestMap.get("productDetails"));
		bill.setCreatedBy(jwtFilter.getCurrentUser());
		billRepository.save(bill);

	}

	private boolean validateRequestMap(Map<String, String> requestMap) {

		if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
				&& requestMap.containsKey("paymentMethod") && requestMap.containsKey("productDetails")
				&& requestMap.containsKey("totalAmount")) {
			return true;
		}
		return false;
	}

}