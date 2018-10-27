import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by haoliu350 on 3/28/17.
 */
public class ByteArrayWatermarkStamper {

    public static final String SRC = "/Users/haoliu350/Desktop/cetelem hp 2.pdf";
    public static final String DEST = "/Users/haoliu350/Desktop/cetelem hp 2_wm004.pdf";
    public static final String IMG = "/Users/haoliu350/Desktop/fitchratings.png";

    public static void main(String[] args) throws IOException, DocumentException {
        //File f = new File(SRC);
        Path pdfPath = Paths.get("/Users/haoliu350/Desktop/cetelem hp 2.pdf");
        byte[] pdf = Files.readAllBytes(pdfPath);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdf);
        byte b = (byte) byteArrayInputStream.read();
        byte[] pdfByteArray =
    }


    public void manipulatePdf2(byte[] src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // text watermark
        Font f = new Font(Font.FontFamily.HELVETICA, 60);
        //Phrase p = new Phrase("Document obtained by Fitch from publicly available sources", f);
        Phrase p = new Phrase("FitchRatings public sources", f);
        // image watermark
        Image img = Image.getInstance(IMG);
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        // transparency
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.2f);
        // properties
        PdfContentByte over;
        Rectangle pagesize;
        float x, y;
        // loop over every page
        for (int i = 1; i <= n; i++) {
            pagesize = reader.getPageSizeWithRotation(i);
            x = (pagesize.getLeft() + pagesize.getRight()) / 2;
            y = (pagesize.getTop() + pagesize.getBottom()) / 2;
            over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            if (i % 2 == 1)
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 50);
            else
                over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
            over.restoreState();
        }
        stamper.close();
        reader.close();
    }
}
