
--du code avec des commentaires (different)

SELECT nom, service --selection (different)
FROM   employe --on parle d'un employe (different)
WHERE  statut = 'stagiaire' --un certain statut de stagiaire (different)
ORDER  BY nom; --on veut l'ordonner par le nom des employes (different)


--la fonction DELETE, comme son nom l'indique, (different)
--sert a supprimer des elements (different)

DELETE FROM a_table
    WHERE field2 = 'N';
	

--la fonction INSERT, comme son nom l'indique, (different)
--sert a insérer des elements (different)

INSERT INTO a_table (field1, field2, field3)
    VALUES ('test', 'N', NULL);

--fin du code avec les commentaires (different)