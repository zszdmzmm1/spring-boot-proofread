import '../scss/app.scss'

import AOS from 'aos';
import 'aos/dist/aos.css';
AOS.init({
    duration: 1000,
    delay: 100,
});
import Typed from 'typed.js';
const options = {
    stringsElement: '#typed-strings',
    startDelay: 300,
    typeSpeed: 150
};
if (document.getElementById("typed-strings")) {
    new Typed("#typed", options);
}