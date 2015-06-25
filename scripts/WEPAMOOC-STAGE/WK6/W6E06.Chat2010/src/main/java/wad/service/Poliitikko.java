package wad.service;

import java.util.Random;
import org.springframework.stereotype.Service;
import wad.domain.Message;

@Service
public class Poliitikko {

    private final String[] lentavatLauseet = {
        "EU on kuin playboyn avioliitto: vielä kasassa, mutta valhetta täynnä.",
        "EU:n halu suojella susia on ymmärrettävää, onhan se susi itsekin.",
        "On yhtä todennäköistä pystyä istuttamaan kookospähkinälle aivot kuin saada EU toimimaan.",
        "Pienelle kansalle riittää pieni valhe – suurelle kansanjoukolle tarvitaan jo EU.",
        "Meidän asioistamme päättää entistä enemmän kreikkalainen kommunisti, italialainen fasisti, saksalainen pankkiiri ja ranskalainen sosialisti.",
        "EU on järjestelmä, jossa punasilmäiset tekevät Brysselissä päätöksiä, joita sinisilmäiset Helsingissä toteuttavat.",
        "Suomen maatiloista on tullut EU-plantaaseja. Voimattomina katselevat entiset isännät peltosarkojaan, joiden pientareilla Brysselin tarkastajat astelevat omistajan elkein kuin kuovit.",
        "Kotimaisia ruumiita ulkomaisista sodista! Ja kenen luvalla? Ei ainakaan äitien.",
        "Sinkkiarkussa on hiljaista. Enää ei kuulu omantunnon ääni.",
        "Demarit voisivat ajaa rahayksikön muutosta eurosta demariksi. Yksi demari olisi sata tunaria.",
        "Nyt ei enää kuku kepun käki – sen jo Suomen kansa näki.",
        "Kemijärvi-kepu on tämän päivän Valco-Sorsa.",
        "Omilla aivoilla ajattelu on kepun ja SDP:n eduskuntaryhmissä rangaistava teko. Laula siis ryhmässä vaikka kuinka tyhmässä.",
        "Valtaa EU:ssa käyttävät sosialistit ja euro-kokoomus. Kansallinen kokoomus on historiallinen ekomerkki, jolla kerätään apteekkarien äänet.",
        "Kyllä kokoomus tuntee tien Suomen kansan takataskulle.",
        "Samansukuisia olentoja ovat selkärankainen vihreä, rehellinen autokauppias, hymyilevä fasisti, onnellinen kommunisti ja tyytyväinen feministi.",
        "Jos kaikki eläisivät kuin Jörn Donner, ratikat eivät kulkisi ajoissa.",
        "RKP on politiikan Hannu Hanhi, joka säästä ja vaalituloksesta huolimatta kelpaa kärrynrasvaksi porvarihallituksen vankkureihin. Se on historian sitkeä surkastuma, jonka kuivumista odotan innolla.",
        "Olen uskon ja aatteen mies, heteromies, lihansyöjä, isä, aviomies, ravimies ja näköistaiteen ystävä.",
        "Jos opetuslapset olisivat uskoneet konsensukseen, siinä olisi jäänyt evankeliumi levittämättä.",
        "Politiikka ei ole yksinkertaista, mutta se ei merkitse, etteikö poliitikko voisi olla yksinkertainen.",
        "Optio on sileäkätisen saavutettu etu.",
        "Seteliselkärankainen poliitikko ja optioselkärankainen yritysjohtaja ovat saman puun hedelmiä.",
        "Mikään yhteiskunta ei ole köyhtynyt köyhiä auttamalla.",
        "Säännöllisin väliajoin taloushistoriassa koittaa aika, jolloin hölmöt erotetaan heidän rahoistaan.",
        "Joosefin maailman ensimmäinen suhdanneoppi seitsemästä lihavasta ja seitsemästä laihasta vuodesta on vielä tänäänkin kestävää tavaraa.",
        "Kukkahattu ja gepardihattu kuuluvat samaan ravintoketjuun. Molemmilla on käsi sinun taskussasi.",
        "En ymmärrä feministejä, eivätkä he minua. Tästä meillä on yhteisymmärrys.",
        "Hedelmällisyyshoitoa vihanneksille. Rajansa kaikella, sanoi rusketus pakaralla.",
        "Ike lähti, Ikenet tulivat.",
        "Alexin juttu on EU, tuttu juttu, mätä huttu."};

    public Message getMessage() {
        Message msg = new Message();
        msg.setContent(lentavatLauseet[new Random().nextInt(lentavatLauseet.length)]);
        msg.setUsername("TS");
        return msg;
    }
}
