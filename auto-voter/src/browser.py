from selenium import webdriver

import logging
from selenium.webdriver.remote.remote_connection import LOGGER
LOGGER.setLevel(logging.WARNING)

user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36"
executable_path = "/usr/bin/chromedriver"


def get_driver(proxy=""):
    chrome_options = webdriver.ChromeOptions()

    chrome_options.add_argument("--no-sandbox")  # https://stackoverflow.com/a/50725918/1689770
    chrome_options.add_argument("--disable-infobars")  # https://stackoverflow.com/a/43840128/1689770
    chrome_options.add_argument("--disable-browser-side-navigation")  # https://stackoverflow.com/a/49123152/1689770
    # https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument('--headless')
    chrome_options.add_argument(f"user-agent={user_agent}")

    chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])  # remove DevTool logging
    prefs = {"profile.managed_default_content_settings.images": 2}
    chrome_options.add_experimental_option("prefs", prefs)  # to remove images

    if len(proxy) > 0:
        chrome_options.add_argument(f"--proxy-server={proxy}")

    driver = webdriver.Chrome(executable_path=executable_path, options=chrome_options)
    driver.set_page_load_timeout(60)

    return driver
