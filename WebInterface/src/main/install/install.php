<?php
/**
 * Created by IntelliJ IDEA.
 * User: Dustin
 * Date: 18.04.13
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */
try {
    $phar_arhive = new Phar("SecureBan-WI.phar");
    $phar_arhive->extractTo(realpath("./../"), null, true);
    echo "extracted successfully files<br/>";
    file_get_contents("./sql/screenshot.sql");
    file_get_contents("./sql/dispute-comment.sql");
    file_get_contents("./sql/config.sql");
    /**
     * need to run sql scripts
     */
} catch (Exception $e) {
    echo "Error while installing SecureBan WebInterface";
    echo "Reason: " . $e->getMessage() . "<br/>";
    foreach ($e->getTrace() as $row) {
        echo $row . "<br/>";
    }
}
?>