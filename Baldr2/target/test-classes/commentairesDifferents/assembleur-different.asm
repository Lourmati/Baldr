;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pseudo-licence.  (different)                       ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


; initialisation des variables (different)
.bss
lecture: resb 64 ; va servir à la lecture (different)
ecriture: resb 64 ; va servir à l'écrture (different)

; code pour la lecture (different)
mov eax, 3 ; registre eax (different)
mov ebx, 0 ; registre ebx (different)
mov ecx, lecture ; registre ecx (different)
mov edx, 64 ; registre edx (different)
int 0x80 ; fin d'instruction (different)


; code pour l'écriture (different)
mov eax, 4 ; registre eax (different)
mov ebx, 0 ; registre ebx (different)
mov ecx, ecriture ; registre ecx (different)
mov edx, 64 ; registre edx (different)
int 0x80 ; fin d'instruction (different)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fin de programme (different);;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;