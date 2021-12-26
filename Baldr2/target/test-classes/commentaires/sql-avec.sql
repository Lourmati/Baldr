
--du code avec des commentaires

SELECT nom, service --selection
FROM   employe --on parle d'un employe
WHERE  statut = 'stagiaire' --un certain statut de stagiaire
ORDER  BY nom; --on veut l'ordonner par le nom des employes


--la fonction DELETE, comme son nom l'indique, 
--sert a supprimer des elements

DELETE FROM a_table
    WHERE field2 = 'N';
	

--la fonction INSERT, comme son nom l'indique, 
--sert a insérer des elements

INSERT INTO a_table (field1, field2, field3)
    VALUES ('test', 'N', NULL);

--fin du code avec les commentaires