package ca.qc.bdeb.baldr.formattage;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
/**
 *
 * @author 1662835
 */
public class ExtrairePDF {
    
    InputStream InputStreamConcat = new InputStream() {
        @Override
        public int read() throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    
        public String  ExtrairePDF (File file) throws IOException {
//        URL  url = Main.class.getResource("/test.pdf");        
        PDDocument doc = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        int pages = doc.getNumberOfPages();
        stripper.setLineSeparator("\n");
        stripper.setStartPage(1);
        stripper.setEndPage(pages);
        String s = stripper.getText(doc);
        doc.close();
        return s;
    }
        
        
        
       public String ExtractImagePdf(File file) {
           
           
       List<DataBuffer> listeTableauBytes = new ArrayList<>();
        
        //Vector<InputStream> streams = new Vector<>();
        
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        
        
        try (final PDDocument document = PDDocument.load(file)) {
            
            PDPageTree list = document.getPages();
            for (PDPage page : list) {
                PDResources pdResources = page.getResources();
                //int i = 1;
                for (COSName name : pdResources.getXObjectNames()) {
                    PDXObject o = pdResources.getXObject(name);
                    if (o instanceof PDImageXObject) {
                        PDImageXObject image = (PDImageXObject) o;
                        
                        BufferedImage bImage = image.getImage();
                        
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        
                        ImageIO.write(bImage, "bmp", bos );
                        
                        byte [] data = bos.toByteArray();
                        
                        outputStream.write(data);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Exception while trying to extract pdf document - " + e);
        }

        
        byte[] totalOfBytesImage = outputStream.toByteArray( );
        String str = new String(totalOfBytesImage);
        
        
        return str;
    }  

   
        
        
}
