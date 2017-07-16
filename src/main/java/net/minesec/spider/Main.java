package net.minesec.spider;

import net.minesec.rules.Rules;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.mitm.CertificateSniffingMitmManager;
import org.littleshoot.proxy.mitm.RootCertificateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.InetSocketAddress;

import static org.openqa.selenium.chrome.ChromeOptions.CAPABILITY;

/**
 * Copyright (c) 10-7-17, MineSec. All rights reserved.
 */
class Main {

    private static final String CAPABILITY_HEADLESS = "headless";

    static {
        System.setProperty("webdriver.chrome.driver", "/opt/chromedriver/chromedriver");
    }

    private HttpProxyServer createProxy() throws RootCertificateException {
        return DefaultHttpProxyServer.bootstrap()
                .withAllowLocalOnly(true)
                .withTransparent(true)
                .withFiltersSource(new RuleCallingHttpFiltersSource())
                .withManInTheMiddle(new CertificateSniffingMitmManager())
                .withPort(8080)
                .start();
    }

    private WebDriver createWebDriver(HttpProxyServer httpProxyServer) {
        InetSocketAddress address = httpProxyServer.getListenAddress();
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        requireCapabilityChromeHeadless(capabilities);
        requireCapabilityProxy(String.format("%s:%d", address.getHostName(), address.getPort()), capabilities);
        return new ChromeDriver(ChromeDriverService.createDefaultService(), capabilities);
    }

    private void requireCapabilityProxy(String httpProxy, DesiredCapabilities capabilities) {
        Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setSslProxy(httpProxy);
        capabilities.setCapability(CapabilityType.PROXY, proxy);
    }

    private void requireCapabilityChromeHeadless(DesiredCapabilities capabilities) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(CAPABILITY_HEADLESS);
        capabilities.setCapability(CAPABILITY, chromeOptions);
    }

    public static void main(String[] args) throws InterruptedException, RootCertificateException {
        Main main = new Main();
        HttpProxyServer httpProxyServer = main.createProxy();
        WebDriver webDriver = main.createWebDriver(httpProxyServer);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 30);
        webDriverWait.until(webDriver1 -> ((JavascriptExecutor) webDriver1).executeScript("return document.readyState").equals("complete"));
        Rules.PAGE_RULES.parallelStream().forEach(pageRule -> pageRule.process(webDriver));
        webDriver.quit();
    }
}
