#
#Prima bisogna creare un database
#

create database arena;

#
#Poi si usa quel database
#

use arena;

#
#Infine si creano le tabelle
#
#
#Struttura della tabella `accounts`
#

CREATE TABLE `accounts` (
  `Name` varchar(32) NOT NULL default '',
  `Password` varchar(32) NOT NULL default '',
  `FirstName` varchar(32) NOT NULL default '',
  `Surname` varchar(32) NOT NULL default '',
  `Admin` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`Name`)
) TYPE=MyISAM;

#
# Struttura della tabella `tournaments`
#

CREATE TABLE `tournaments` (
  `Name` varchar(32) NOT NULL default '',
  `Type` int(11) NOT NULL default '0',
  `Game` varchar(32) NOT NULL default '',
  `Description` text NOT NULL,
  `Players` text NOT NULL,
  `Owner` varchar(32) NOT NULL default '',
  `Open` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`Name`)
) TYPE=MyISAM;

#
# Struttura della tabella `matches`
#

CREATE TABLE `matches` (
  `Name` varchar(32) NOT NULL default '',
  `P1` varchar(32) NOT NULL default '',
  `P2` varchar(32) NOT NULL default '',
  `Played` tinyint(1) NOT NULL default '0',
  `Result` int(11) NOT NULL default '0',
  `Tournament` varchar(32) NOT NULL default '',
  `Game` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`Name`)
) TYPE=MyISAM;