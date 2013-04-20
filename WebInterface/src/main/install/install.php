<?php
/**
 * Created by IntelliJ IDEA.
 * User: Dustin
 * Date: 18.04.13
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */

define("BASEPATH", "");
require_once realpath("./../application/config/database.php");

try {
    $pdo = new PDO($db[$active_group]['dbdriver'] . ":host=" . $db[$active_group]['hostname'] . ";dbname=" . $db[$active_group]['database'], $db[$active_group]['username'], $db[$active_group]['password']);

    foreach (glob(realpath("./sql") . "/*.sql") as $sql_name) {
        echo "installing SQL " . $sql_name . " ... ";
        $sql = file_get_contents($sql_name);
        $pdo->exec($sql);
        $error = $pdo->errorInfo();
        if ($error[2] != "") {
            throw new Exception($error[2]);
        }
        echo "OK<br/>";
    }

} catch (Exception $e) {
    echo "FAILED Reason: " . $e->getMessage() . "<br/>";
}
?>