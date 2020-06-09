import random
import browser
import proxies
from colored import fg
from multiprocessing import Pool

from decorators import vote_wrapper


@vote_wrapper(
    name="McServ",
    url="http://www.mcserv.org/Rubidia_4736.html",
    message="Vous ne pouvez voter qu'une seule fois toutes les 6 heures."
)
def vote_mcserv(driver):
    vote_button = driver.find_element_by_id("vote4736")
    vote_button.click()


@vote_wrapper(
    name="serveurs-minecraft.org",
    url="https://www.serveurs-minecraft.org/vote.php?id=43214",
    message="Vous avez déjà voté aujourd'hui !"
)
def vote_serveurs_minecraft_org(driver):
    vote_form = driver.find_element_by_id("form_vote")
    vote_form.submit()


@vote_wrapper(
    name="serveurs-minecraft.com",
    url="https://www.serveurs-minecraft.com/serveur-minecraft?Classement=Rubidia",
    message="Vous avez déjà voté aujourd'hui !"
)
def vote_serveurs_minecraft_com(driver):
    vote_button = driver.find_element_by_css_selector("a.btn.btn-lg.btn-info")
    vote_button.click()

    vote_button = driver.find_element_by_css_selector("input[value='Valider le vote pour Rubidia !']")
    vote_button.click()


def vote(proxy):
    print(fg(3), f"Testing proxy {proxy}...")
    driver = browser.get_driver(proxy)

    vote_serveurs_minecraft_org(driver)
    vote_serveurs_minecraft_com(driver)
    vote_mcserv(driver)


if __name__ == "__main__":
    proxies = proxies.get_online()
    total = len(proxies)

    import multiprocessing
    print(fg(3), f"Running on {multiprocessing.cpu_count()} CPUs!")

    with Pool() as pool:
        random.shuffle(proxies)
        for i, _ in enumerate(pool.imap(vote, proxies)):
            print(fg(3), f"[{i + 1}/{total}] Done!")
