<?php

require __DIR__ . '/com/swayam/ocr/porua/rest/IndexController.php';

use DI\Bridge\Slim\Bridge;
use com\swayam\ocr\porua\rest\IndexController;

$container = require __DIR__ . '/com/swayam/ocr/porua/config/bootstrap.php';

$app = Bridge::create($container);

$app->get('/', [IndexController::class, 'get']);

$app->run();
?>
