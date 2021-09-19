package ru.scratty.izbirkomparser.uikpageparser

import net.sourceforge.tess4j.Tesseract
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import ru.scratty.izbirkomparser.uikparser.Uik
import java.io.Closeable
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

class UikPageParser : Closeable {

    companion object {
        private const val URL =
            "http://www.vybory.izbirkom.ru/region/izbirkom?action=show&root=0&tvd=%d&vrn=100100067795849&prver=0&pronetvd=null&region=0&sub_region=0&type=242&report_mode=null"

        private const val WAIT_TIMEOUT = 30 * 60L
    }

    private val driver: ChromeDriver

    private val tesseract = Tesseract().apply {
        setDatapath("src/main/resources/tessdata")
        setLanguage("rus")
        setPageSegMode(6)
        setOcrEngineMode(1)
        setOcrEngineMode(1)
    }

    init {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/selenium/chromedriver")

        val options = ChromeOptions().addArguments("--start-fullscreen")

        driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
    }

    fun setZoom(zoom: Double) {
        driver.get("chrome://settings/")
        driver.executeScript("chrome.settingsPrivate.setDefaultZoom($zoom);")
    }

    fun parseUikPage(uik: Uik): UikPage {
        val url = URL.format(uik.id)
        driver.get(url)

        val wait = WebDriverWait(driver, WAIT_TIMEOUT)
        wait.until {
            driver.findElement(By.cssSelector("table.table-bordered"))
                .findElements(By.tagName("tr"))
                .size > 10
        }

        val name = parseName()
        val stats = parseStats()

        return UikPage(uik.id, url, name, stats)
    }

    private fun parseName(): String = driver.findElement(By.cssSelector("ul.breadcrumb"))
        .text
        .replace("\n⁄\n", "/")
        .replace("\n⁄  ", "/")

    private fun parseStats(): List<Int?> = (1..32).map {
        driver.executeScript("document.querySelectorAll('td.text-right')[$it].scrollIntoView();")

        val imgFile = driver.getScreenshotAs(OutputType.FILE)

        val sourceImg = ImageIO.read(imgFile)

        val x = 3120
        val y = 0
        val w = sourceImg.width - x - 100
        val h = 55

        val croppedImg = sourceImg.getSubimage(x, y, w, h)
        val num = tesseract.doOCR(croppedImg)
            .replace("[\\D]".toRegex(), "")
            .toIntOrNull()

        num
    }

    override fun close() {
        driver.close()
    }
}