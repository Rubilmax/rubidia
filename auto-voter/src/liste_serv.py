from bs4 import BeautifulSoup

import time
from colored import fg
from multiprocessing import Pool

import browser
from decorators import proxies_getter, vote_wrapper


def vote_liste_serveur_minecraft(index):
    driver = browser.get_driver()
    count = 0

    try:
        driver.get("http://www.liste-serveur-minecraft.net/serveur-minecraft?Liste-Serveur-Minecraft=Rubidia")

        success_message = "Merci ! Votre vote a bien été enregistré :)"
        content = BeautifulSoup(driver.page_source, "html.parser").text

        while success_message not in content:
            vote_input = driver.find_element_by_css_selector("input[name='captchaCode']")
            vote_input.send_keys(str(index))

            vote_button = driver.find_element_by_css_selector("button.btn.background5")
            vote_button.click()

            content = BeautifulSoup(driver.page_source, "html.parser").text
            count += 1

        print(fg(2), f"({index}):: Success after {count} attempts!")
    except Exception as ex:
        print(fg(1), ex)

    driver.close()


if __name__ == "__main__":

    iterations = list(range(3, 8)) * 1000
    total = len(iterations)

    import multiprocessing
    print(fg(3), f"Running on {multiprocessing.cpu_count()} CPUs!")

    with Pool() as pool:
        for i, _ in enumerate(pool.imap(vote_liste_serveur_minecraft, iterations)):
            print(fg(3), f"[{i + 1}/{total}] Done!")
