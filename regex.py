import re

with open('log.txt', "r") as base_datos:
    log = base_datos.readlines()

regex = '(T1-)(.*?)((T2-)(.*?)(T11-)|(T3-)(.*?)(T4-)(.*?)(T5-)(.*?)(T6-))(.*?)|(T7-)(.*?)(T8-)(.*?)(T9-)(.*?)(T10-)(.*?)' #detecta los 3 t-invariantes

groups = '\g<2>\g<5>\g<8>\g<10>\g<12>\g<14>\g<16>\g<18>\g<20>\g<22>' #son los tokens que va a seguir analizando

log = re.subn(regex, groups, log[0])
while(log[1] != 0): #mientras encuentre coincidencias sigue buscando
    print(log)  
    log = re.subn(regex, groups, log[0])

if(log[0] != "\n"): #cuando encontro todos invariantes queda un salto de linea solamente
    print("Fail")
else: 
    print("OK")